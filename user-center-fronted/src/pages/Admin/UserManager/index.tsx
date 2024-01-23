import {  PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import {Button, Image,Space,Table} from 'antd';
import { useRef } from 'react';
import {searchUsers} from "@/services/ant-design-pro/api";
export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

// type CurrentUser = {
//   url: string;
//   id: number;
//   number: number;
//   title: string;
//   labels: {
//     name: string;
//     color: string;
//   }[];
//   state: string;
//   comments: number;
//   created_at: string;
//   updated_at: string;
//   closed_at?: string;
// };

const columns: ProColumns<API.CurrentUser>[] = [
  {
    title: '编号',
    dataIndex: 'id',
    valueType: 'indexBorder',
    width: 48,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    //copyable: true, //是否可复制
    ellipsis: true, //是否允许缩略
    tip: '标题过长会自动收缩',
    //搜索时是不是必填项
    // formItemProps: {
    //   rules: [
    //     {
    //       required: true,
    //       message: '此项为必填项',
    //     },
    //   ],
    // },
  },
  {
    title: '用户账户',
    dataIndex: 'userAccount',
    hideInSearch: true,
  },
  {
    title: '头像',
    dataIndex: 'avatarUrl',
    render: (_,record)=>(
       <div>
         <Image src={record.avatarUrl} width={80} />
       </div>
    ),
    search: false
  },
  {
    title: '性别',
    dataIndex: 'gender',
    search: false,
    valueType: 'select',
    valueEnum: {
      0: {text: '女'},
      1: {text: '男'}
    }
  },
  {
    title: '电话',
    dataIndex: 'phone',
  },
  {
    title: '邮件',
    dataIndex: 'email',

  },
  {
    title: '状态',
    dataIndex: 'userStatus',
    hideInSearch: true,
    valueType: 'select',
      valueEnum: {
  0: {text: '正常',status: 'Processing'},
  1: {
    text: '异常',
      status: 'Error'
  }
}
  },
  {
    title: '角色',
    dataIndex: 'userRole',
    hideInSearch: true,
    valueType: 'select',
    valueEnum: {
      0: {text: '普通用户',status: 'Default'},
      1: {
        text: '管理员',
        status: 'Success'
      }
    }
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    valueType: 'dateTime',
    hideInSearch: true,
    editable: false
  },

  {
    title: '操作',
    valueType: 'option',
    key: 'option',
    render: (text, record, _, action) => [
      <a
        key="editable"
        onClick={() => {
          action?.startEditable?.(record.id);
        }}
      >
        编辑
      </a>,
      <a href={record.avatarUrl} target="_blank" rel="noopener noreferrer" key="view">
        查看
      </a>,
      <TableDropdown
        key="actionGroup"
        onSelect={() => action?.reload(false)}
        menus={[
          { key: 'copy', name: '复制' },
          { key: 'delete', name: '删除' },
        ]}
      />,
    ],
  },
];

export default () => {
  const actionRef = useRef<ActionType>();
  // @ts-ignore
  return (
    <ProTable<API.CurrentUser>
      columns={columns}
      actionRef={actionRef}
      cardBordered
      request={async (params, sort, filter) => {
        console.log(sort, filter);
        await waitTime(1000);
        const userLists=await searchUsers(params);
        return{
          data: userLists
        }

      }}
      //选择框
      rowSelection={{
        // 自定义选择项参考: https://ant.design/components/table-cn/#components-table-demo-row-selection-custom
        // 注释该行则默认不显示下拉选项
        selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT],
      }}
      tableAlertRender={({
                           selectedRowKeys,
                           selectedRows,
                           onCleanSelected,
                         }) => {
        console.log(selectedRowKeys, selectedRows);
        return (
          <Space size={24}>
            <span>
              已选 {selectedRowKeys.length} 项
              <a style={{ marginInlineStart: 8 }} onClick={onCleanSelected}>
                取消选择
              </a>
            </span>
          </Space>
        );
      }}
      tableAlertOptionRender={() => {
        return (
          <Space size={16}>
            <a>批量删除</a>
            <a>导出数据</a>
          </Space>
        );
      }}

      editable={{
        type: 'multiple',
        onSave: async (rowKey, data, row) => {
          console.log(rowKey, data, row);
          await waitTime(1000);
        },

      }}
      columnsState={{
        persistenceKey: 'pro-table-singe-demos',
        persistenceType: 'localStorage',
        defaultValue: {
          option: { fixed: 'right', disable: true },
        },
        onChange(value) {
          console.log('value: ', value);
        },
      }}
      rowKey="id"
      search={{
        labelWidth: 'auto',
      }}
      options={{
        setting: {
          //@ts-ignore
          listsHeight: 400,
        },
      }}
      form={{
        // 由于配置了 transform，提交的参与与定义的不同这里需要转化一下
        syncToUrl: (values, type) => {
          if (type === 'get') {
            return {
              ...values,
              created_at: [values.startTime, values.endTime],
            };
          }
          return values;
        },
      }}
      pagination={{
        pageSize: 5,
        onChange: (page) => console.log(page),
      }}
      dateFormatter="string"
      headerTitle="表格数据"
      toolBarRender={() => [
        <Button
          type="primary"
          onClick={() => {
            actionRef.current?.addEditRecord?.({
              id: (Math.random() * 1000000).toFixed(0),
              title: '新的一行',
            });
          }}
          icon={<PlusOutlined />}
        >
          新建
        </Button>

      ]}
    />
  );
};
